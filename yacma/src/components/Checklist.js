import React from 'react'
import Navbar from './Navbar'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import { Link } from 'react-router-dom'
import { Spin, Button, message, Tooltip } from 'antd'
import fetch from 'isomorphic-fetch'
import CreateItem from './CreateItem'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.props = props
    this.handleDelete = this.handleDelete.bind(this)
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
        this.props.history.push('/checklists')
      })
      .catch(ex => message.error('Cannot delete checklist'))
  }
  render () {
    const encoded = cookies.get('auth')
    const header = {
      method: 'GET',
      headers: {
        'Authorization': `Basic ${encoded}`,
        'Access-Control-Allow-Origin': '*'
      }
    }
    const path = this.props.location.pathname
    const checklistId = path.split('/')[2]
    const url = config.API.PATH + '/api/checklists/' + checklistId
    return (
      <div>
        <div>
          <Navbar />
        </div>
        <div>
          <div>
            <HttpGet url={url} headers={header}
              render={(result) => (
                <div>
                  <HttpGetSwitch
                    result={result}
                    onLoading={() => <div><Spin id='spin' tip='Loading Checklist...' /></div>}
                    onError={_ => (
                      <div>
                        <h1>Error getting the List, maybe it doesn't exist or you don't have permission to see it !! </h1>
                      </div>
                    )}
                    onJson={json => (
                      <div>
                        <h1 class='displayBySide'><strong>{json.properties.name}</strong></h1>
                        <Tooltip placement='right' title='Remove this resource'>
                          <Button
                            id='removeListBtn'
                            type='danger'
                            size='large'
                            icon='delete'
                            shape='circle'
                            onClick={() => this.handleDelete(json)}
                          />
                        </Tooltip>
                        <h1><strong>Description</strong> : {json.properties.description}</h1>
                        <h1><strong>Completion Date</strong> : {json.properties.completionDate}</h1>
                        <HttpGet
                          url={config.API.PATH + json.entities.find((entity) => entity.class.includes('items')).href}
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
                                                    pathname: `/checklists/${item.properties.checklistId}/items/${item.properties.itemId}`
                                                  }}>
                                                    {`${item.properties.name}`}</Link>
                                                </li>
                                            )
                                          }
                                        </ul>
                                        <CreateItem url={json.entities.find((entity) => entity.class.includes('items')).href} />
                                      </div>
                                    )
                                  }
                                  return (
                                    <div>
                                      <h1>No Items yet</h1>
                                      <CreateItem url={json.entities.find((entity) => entity.class.includes('items')).href} />
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
        <Button
          id='backBtn'
          type='primary'
          icon='left'
          onClick={() => this.props.history.push(`/checklists`)}>
          Checklists
        </Button>
      </div>
    )
  }
}
