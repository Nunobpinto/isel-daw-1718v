import React from 'react'
import Navbar from './Navbar'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import { Link } from 'react-router-dom'
import { Spin } from 'antd'
import CreateItemTemplate from './CreateItemTemplate'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.props = props
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
    const templateId = path.split('/')[2]
    const url = config.API_PATH + '/api/templates/' + templateId
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
                        <h1><strong>Name</strong> : {json.properties.name}</h1>
                        <h1><strong>Description</strong> : {json.properties.description}</h1>
                        <h1><strong>State</strong> : {json.properties.state}</h1>
                        <HttpGet
                          url={config.API_PATH + json.entities.find((entity) => entity.class.includes('item-templates')).href}
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
