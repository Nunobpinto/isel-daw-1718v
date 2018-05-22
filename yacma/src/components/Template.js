import React from 'react'
import Navbar from './Navbar'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import { Link } from 'react-router-dom'
import { Spin, Button, message, Popover, Tooltip } from 'antd'
import fetch from 'isomorphic-fetch'
import CreateItemTemplate from './CreateItemTemplate'
import CreateChecklist from './CreateChecklist'
const cookies = new Cookies()

export default class Template extends React.Component {
  constructor (props) {
    super(props)
    this.props = props
    this.handleDelete = this.handleDelete.bind(this)
    this.state = {
      updateAction: false
    }
  }

  handleDelete (object) {
    const action = object.actions.find(act => act.method === 'DELETE')
    const encoded = cookies.get('auth')
    const header = {
      method: 'DELETE',
      headers: {
        'Authorization': `Basic ${encoded}`,
        'Access-Control-Allow-Origin': '*'
      }
    }
    const uri = config.API.PATH + action.href
    fetch(uri, header)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.props.history.push('/templates')
      })
      .catch(ex => message.error('Cannot delete template'))
  }

  retrieveCreateAction = (object) => object.actions.find(act => act.name === 'create-checklist')
  

  render () {
    const path = this.props.location.pathname
    const templateId = path.split('/')[2]
    const encoded = cookies.get('auth')
    const header = {
      method: 'GET',
      headers: {
        'Authorization': `Basic ${encoded}`,
        'Access-Control-Allow-Origin': '*'
      }
    }
    const url = config.API.PATH + '/api/templates/' + templateId
    return (
      <div>
        <div>
          <Navbar />
        </div>
        <div>
          <h1>Template with id = {templateId}</h1>
          <div>
            <HttpGet url={url} headers={header}
              render={(result) => (
                <div>
                  <HttpGetSwitch
                    result={result}
                    onLoading={() => <div><Spin id='spin' tip='Loading Template...' /></div>}
                    onError={_ => (
                      <div>
                        <h1>Error getting the Template List, maybe it doesn't exist or you don't have permission to see it !! </h1>
                      </div>
                    )}
                    onJson={json => (
                      <div>
                        <Tooltip title='Remove this resource'>
                          <Button
                            type='danger'
                            size='large'
                            icon='delete'
                            shape='circle'
                            onClick={() => this.handleDelete(json)}
                          />
                        </Tooltip>
                        <h1><strong>Name</strong> : {json.properties.name}</h1>
                        <h1><strong>Description</strong> : {json.properties.description}</h1>
                        <Popover content = {<CreateChecklist url={this.retrieveCreateAction(json).href}/>}>
                          <Button type='primary'>Create checklist from this template</Button>
                        </Popover>
                        <HttpGet
                          url={config.API.PATH + json.entities.find((entity) => entity.class.includes('item-templates')).href}
                          headers={header}
                          render={(result) => (
                            <div>
                              <HttpGetSwitch
                                result={result}
                                onLoading={() => <div><Spin id='spin' tip='Loading Items...' /></div>}
                                onJson={resp => {
                                  if (resp.entities) {
                                    return (
                                      <div>
                                        <h1>Items :</h1>
                                        <ul>
                                          {
                                            resp.entities.map(
                                              item =>
                                                <li key={item.properties.itemId}>
                                                  <Link to={{
                                                    pathname: `/templates/${item.properties.templateId}/items/${item.properties.itemTemplateId}`
                                                  }}>
                                                    {`${item.properties.name}`}</Link>
                                                </li>
                                            )
                                          }
                                        </ul>
                                        <CreateItemTemplate url={json.entities.find((entity) => entity.class.includes('item-templates')).href} />
                                      </div>
                                    )
                                  }
                                  return (
                                    <div>
                                      <h1>No Items yet</h1>
                                      <CreateItemTemplate url={json.entities.find((entity) => entity.class.includes('item-templates')).href} />
                                    </div>
                                  )
                                }} />
                            </div>)}
                        />
                      </div>
                    )} />
                </div>)} />
          </div>
        </div>
      </div>
    )
  }
}
