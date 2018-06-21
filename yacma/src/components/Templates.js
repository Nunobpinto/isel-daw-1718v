import React from 'react'
import Navbar from './Navbar'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import {Link} from 'react-router-dom'
import {Spin} from 'antd'
import CreateTemplate from './CreateTemplate'
const cookies = new Cookies()

export default () => {
  const token = cookies.get('auth')
  const header = {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Access-Control-Allow-Origin': '*'
    }
  }

  const url = config.API.PATH + '/api/templates'
  return (
    <div>
      <div>
        <Navbar />
      </div>
      <div>
        <h1>All my templates</h1>
        <div>
          <HttpGet url={url} headers={header}
            render={(result) => (
              <div>
                <HttpGetSwitch
                  result={result}
                  onLoading={() => <div><Spin id='spin' tip='Loading Templates...' /></div>}
                  onJson={json => {
                    if (json.entities) {
                      return (
                        <div>
                          <ul>
                            {
                              json.entities.map(
                                item =>
                                  <li key={item.properties.templateId}>
                                    <Link to={{
                                      pathname: `templates/${item.properties.templateId}`
                                    }}>
                                      {`${item.properties.name}`}</Link>

                                  </li>
                              )
                            }
                          </ul>
                          <CreateTemplate url={json.actions[1].href} />
                        </div>
                      )
                    }
                    return (
                      <div>
                        <h1>No Templates yet</h1>
                        <CreateTemplate url={json.actions[1].href} />
                      </div>
                    )
                  }}
                />
              </div>
            )} />
        </div>
      </div>
    </div>
  )
}
