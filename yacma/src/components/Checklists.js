import React from 'react'
import Navbar from './Navbar'
import CreateChecklist from './CreateChecklist'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import {Link} from 'react-router-dom'
import {Spin} from 'antd'
const cookies = new Cookies()

export default () => {
  const encoded = cookies.get('auth')
  const header = {
    method: 'GET',
    headers: {
      'Authorization': `Basic ${encoded}`,
      'Access-Control-Allow-Origin': '*'
    }
  }

  const url = config.API.PATH + '/api/checklists'
  return (
    <div>
      <div>
        <Navbar />
      </div>
      <div>
        <h1>All my checklists</h1>
        <div>
          <HttpGet url={url} headers={header}
            render={(result) => (
              <div>
                <HttpGetSwitch
                  result={result}
                  onLoading={() => <div><Spin id='spin' tip='Loading Checklists...' /></div>}
                  onJson={json => {
                    if (json.entities) {
                      return (
                        <div>
                          <ul>
                            {
                              json.entities.map(
                                item =>
                                  <li key={item.properties.checklistId}>
                                    <Link to={{
                                      pathname: `checklists/${item.properties.checklistId}`
                                    }}>
                                      {`${item.properties.name}`}</Link>
                                  </li>
                              )
                            }
                          </ul>
                          <CreateChecklist url={json.actions[1].href} />
                        </div>
                      )
                    }
                    return (
                      <div>
                        <h1>No Checklists yet</h1>
                        <CreateChecklist url={json.actions[1].href} />
                      </div>
                    )
                  }} />
              </div>
            )} />
        </div>
      </div>
    </div>
  )
}
