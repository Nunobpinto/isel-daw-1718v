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
  }

  handleDelete (object) {
    const action = object.actions.find(act => act.method === 'DELETE')
    const encoded = cookies.get('auth')
    const checklistId = object.properties.checklistId
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
        this.props.history.push(`/checklists/${checklistId}`)
      })
      .catch(ex => message.error('Cannot delete template'))
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
    const itemId = path.split('/')[4]
    const url = config.API.PATH + '/api/checklists/' + checklistId + '/items/' + itemId
    return (
      <div>
        <div>
          <Navbar />
        </div>
        <div>
          <h1>Item with id = {itemId}</h1>
          <div>
            <HttpGet url={url} headers={header}
              render={(result) => (
                <div>
                  <HttpGetSwitch
                    result={result}
                    onLoading={() => <div><Spin id='spin' tip='Loading Item...' /></div>}
                    onJson={json => (
                      <div>
                        <Tooltip title='Remove this resource'>
                          <Button
                            type='danger'
                            size='large'
                            icon='delete'
                            onClick={() => this.handleDelete(json)}
                          />
                        </Tooltip>
                        <h1><strong>Name</strong> : {json.properties.name}</h1>
                        <h1><strong>Description</strong> : {json.properties.description}</h1>
                        <h1><strong>State</strong> : {json.properties.state}</h1>
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
      </div>
    )
  }
}
