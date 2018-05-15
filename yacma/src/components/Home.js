import React from 'react'
import Navbar from './Navbar'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import { Spin, Avatar } from 'antd'
import config from '../config'

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
  console.log(encoded)
  const decoded = window.atob(encoded)
  const username = decoded.split(':')[0]
  const url = config.API.PATH + '/api/users/' + username
  return (
    <div>
      <div>
        <Navbar />
      </div>
      <div>
        <h1>Welcome to YACMA</h1>
        <div>
          <HttpGet url={url} headers={header}
            render={(result) => (
              <div>
                <HttpGetSwitch
                  result={result}
                  onLoading={() => <div><Spin id='spin' tip='Loading User HomePage...' /></div>}
                  onJson={json => (
                    <div>
                      <Avatar size='large' shape='square' icon='user' />
                      <h1><strong>Username</strong> : {json.properties.username}</h1>
                      <h1><strong>E-Mail</strong> : {json.properties.email}</h1>
                      <h1><strong>Family Name</strong> : {json.properties.familyName}</h1>
                      <h1><strong>Given Name</strong> : {json.properties.givenName}</h1>
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
