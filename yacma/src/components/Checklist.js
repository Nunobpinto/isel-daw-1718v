import React from 'react'
import Navbar from './Navbar'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import {Spin} from 'antd'
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
    const checklistId = path.split('/')[2]
    const url = config.API_PATH + 'api/checklists/' + checklistId
    return (
      <div>
        <div>
          <Navbar />
        </div>
        <div>
          <h1>List with id = {checklistId}</h1>
          <div>
            <HttpGet url={url} headers={header}
              render={(result) => (
                <div>
                  <HttpGetSwitch
                    result={result}
                    onLoading={() => <div><Spin id='spin' tip='Loading Checklist...' /></div>}
                    onJson={json => (
                      <div>
                        <h1><strong>Name</strong> : {json.properties.name}</h1>
                        <h1><strong>Description</strong> : {json.properties.description}</h1>
                        <h1><strong>Completion Date</strong> : {json.properties.completionDate}</h1>
                      </div>
                    )}
                  />
                </div>
              )} />
          </div>
        </div>
      </div>
    )
  }
}