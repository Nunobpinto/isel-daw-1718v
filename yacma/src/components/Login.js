import React from 'react'
import {Redirect} from 'react-router-dom'
import { Button } from 'antd'
import logo from '../logo.svg'
import 'antd/dist/antd.css'
import Cookies from 'universal-cookie'
import { UserManager } from 'oidc-client'
const cookies = new Cookies()

var mitreIDsettings = {
  authority: 'http://localhost:8080/openid-connect-server-webapp',
  client_id: '2ccb1d09-a1b5-4fb2-b2cf-c993d0b085ff',
  redirect_uri: 'http://localhost:3000/redirect.html',
  popup_redirect_uri: 'http://localhost:3000/redirect.html',
  post_logout_redirect_uri: 'http://localhost:3000/user-manager-sample.html',
  response_type: 'token id_token',
  scope: 'openid email profile',
  silent_redirect_uri: 'http://localhost:9000/user-manager-sample-silent.html',
  automaticSilentRenew: true,
  filterProtocolClaims: true,
  loadUserInfo: true
}

const mgr = new UserManager(mitreIDsettings)

class LoginForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      username: '',
      password: '',
      user: undefined
    }
    this.handleSubmit = this.handleSubmit.bind(this)
    this.login = this.login.bind(this)
  }

  login () {
    mgr.getUser()
      .then(user => {
        if (user) {
          this.setState({ user: user })
        } else {
          mgr.signinPopup()
            .then(user => {
              this.setState({ user: user })
            })
        }
      })
  }

  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.setState(() => ({
          username: values.username,
          password: values.password,
          redirect: true
        }))
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
          {this.state.user
            ? <pre>{JSON.stringify(this.state.user, null, 2)}</pre>
            : <Button type='primary' onClick={this.login} className='login-form-button'>Login</Button>

          }
        </div>
      </div>
    )
  }
}

export default LoginForm
