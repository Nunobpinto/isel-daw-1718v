import React from 'react'
import Navbar from './Navbar'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import fetch from 'isomorphic-fetch'
import { Spin, message, Button, Tooltip } from 'antd'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.props = props
    this.handleDelete = this.handleDelete.bind(this)
    this.handleUpdate = this.handleUpdate.bind(this)
    this.handleVisibleChange = this.handleVisibleChange.bind(this)
    this.state = {
      visible: false
    }
  }

  handleVisibleChange (visible) {
    this.setState({ visible })
  }

  handleDelete (object) {
    const action = object.actions.find(act => act.method === 'DELETE')
    const token = cookies.get('auth')
    const checklistId = object.properties.checklistId
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
        this.props.history.push(`/checklists/${checklistId}`)
      })
      .catch(ex => message.error('Cannot delete item'))
  }

  handleUpdate (object) {
    const action = object.actions.find(act => act.method === 'PUT')
    const token = cookies.get('auth')
    const itemState = 'Completed'
    const itemDescription = object.properties.description
    const itemName = object.properties.name
    const data = {
      'name': itemName,
      'description': itemDescription,
      'state': itemState
    }
    const header = {
      method: 'PUT',
      body: JSON.stringify(data),
      headers: {
        'Content-Type': 'application/json',
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
        const mainDiv = document.getElementById('main')
        const h1ToRemove = document.getElementById('state_div')
        const buttonToRemove = document.getElementById('update_button')
        mainDiv.removeChild(h1ToRemove)
        mainDiv.removeChild(buttonToRemove)
        mainDiv.innerHTML += ' <h1><strong>State</strong> : Completed</h1>'
      })
      .catch(ex => message.error('Cannot update item'))
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
    const checklistId = path.split('/')[2]
    const itemId = path.split('/')[4]
    const url = config.API.PATH + '/api/checklists/' + checklistId + '/items/' + itemId
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
                            id='removeListBtn'
                            type='danger'
                            size='large'
                            icon='delete'
                            shape='circle'
                            onClick={() => this.handleDelete(json)}
                          />
                        </Tooltip>
                        <div id='main'>
                          <h1><strong>Description</strong> : {json.properties.description}</h1>
                          <h1 id='state_div'><strong>State</strong> : {json.properties.state}</h1>
                          {json.properties.state === 'Uncompleted'
                            ? <div id='update_button'>
                              <Button
                                id='removeListBtn'
                                type='primary'
                                size='large'
                                icon='check'
                                onClick={() => this.handleUpdate(json)}>
                                Complete this item
                              </Button>
                            </div> : null
                          }
                        </div>
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
          onClick={() => this.props.history.push(`/checklists/${checklistId}`)}>
                          Checklist
        </Button>
      </div>
    )
  }
}
