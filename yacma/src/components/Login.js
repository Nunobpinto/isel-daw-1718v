import React from 'react'
import {Redirect} from 'react-router-dom'
import { Button } from 'antd'
import logo from '../logo.svg'
import 'antd/dist/antd.css'
import Cookies from 'universal-cookie'
import oidc from '../oidcConfig'
import fetch from 'isomorphic-fetch'
import config from '../config'
const cookies = new Cookies()

class LoginForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      user: undefined
    }
    this.login = this.login.bind(this)
  }

  login () {
    oidc().getUser()
      .then(user => {
        if (user) {
          this.setState({ user: user })
        } else {
          oidc().signinPopup()
            .then(user => {
              const token = user.access_token
              cookies.set('auth', token)
              const options = {
                method: 'POST',
                headers: {
                  'Authorization': `Bearer ${token}`,
                  'Access-Control-Allow-Origin': '*'
                }
              }
              fetch(config.API.PATH + '/api/users', options)
                .then(resp => {
                  if (resp.status >= 400) {
                    throw new Error('Unable to access content')
                  }
                  this.props.history.push('/')
                })
                .catch(err => 'Error')
              this.props.history.push('/')
            })
        }
      })
  }

  render () {
    if (cookies.get('auth')) {
      return (<Redirect to='' />)
    }
    return (
      <div className='App'>
        <header className='App-header'>
          <img src={logo} className='App-logo' alt='logo' />
          <h1 className='App-title'>Welcome to YACMA <small>(Yet Another Checklist Management Application)</small></h1>
        </header>
        <div className='App-intro' >
          <Button type='primary' onClick={this.login} className='login-form-button'>Login</Button>
        </div>
      </div>
    )
  }
}

export default LoginForm
