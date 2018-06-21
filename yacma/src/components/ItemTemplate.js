import React from 'react'
import Navbar from './Navbar'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import { Spin, message, Button, Tooltip } from 'antd'
import fetch from 'isomorphic-fetch'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.props = props
    this.handleDelete = this.handleDelete.bind(this)
  }

  handleDelete (object) {
    const action = object.actions.find(act => act.method === 'DELETE')
    const token = cookies.get('auth')
    const checklistTemplateId = object.properties.checklistTemplateId
    const header = {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Access-Control-Allow-Origin': '*'
      }
    }
    const uri = config.API.PATH + action.href
    fetch(uri, header)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.props.history.push(`/templates/${checklistTemplateId}`)
      })
      .catch(ex => message.error('Cannot delete template'))
  }

  render () {
    const token = cookies.get('auth')
    const header = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Access-Control-Allow-Origin': '*'
      }
    }
    const path = this.props.location.pathname
    const templateId = path.split('/')[2]
    const itemId = path.split('/')[4]
    const url = config.API.PATH + '/api/templates/' + templateId + '/items/' + itemId
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
                    onLoading={() => <div><Spin id='spin' tip='Loading Item...' /></div>}
                    onJson={json => (
                      <div>
                        <h1 class='displayBySide'><strong>{json.properties.name}</strong></h1>
                        <Tooltip placement='right' title='Remove this resource'>
                          <Button
                            type='danger'
                            id='removeListBtn'
                            size='large'
                            icon='delete'
                            shape='circle'
                            onClick={() => this.handleDelete(json)}
                          />
                        </Tooltip>
                        <h1><strong>Description</strong> : {json.properties.description}</h1>
                      </div>
                    )}
                    onError={_ => (
                      <div>
                        <h1>Error getting the Item, maybe it doesn't exist or you don't have permission to see it !! </h1>
                      </div>
                    )} />
                </div>)} />
          </div>
        </div>
        <Button
          id='backBtn'
          type='primary'
          icon='left'
          onClick={() => this.props.history.push(`/templates/${templateId}`)}>
                          Template
        </Button>
      </div>
    )
  }
}
