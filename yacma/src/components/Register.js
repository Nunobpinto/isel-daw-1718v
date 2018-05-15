import React from 'react'
import {message, Form, Input, Button, Spin} from 'antd'
import logo from '../logo.svg'
import '../App.css'
import 'antd/lib/button/style/css'
import Cookies from 'universal-cookie'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import {Redirect} from 'react-router-dom'
const cookies = new Cookies()

class RegisterForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      username: '',
      password: '',
      email: '',
      familyName: '',
      givenName: '',
      redirect: false
    }
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.setState(() => ({
          username: values.username,
          password: values.password,
          email: values.email,
          familyName: values.familyName,
          givenName: values.givenName,
          redirect: true
        }))
      }
    })
  }

  render () {
    let {redirect} = this.state
    if (cookies.get('auth')) {
      return (<Redirect to='/' />)
    }
    if (redirect === true) {
      const data = {
        username: this.state.username,
        password: this.state.password,
        email: this.state.email,
        family_name: this.state.familyName,
        given_name: this.state.givenName
      }
      const header = {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json'
        }
      }
      const url = config.API.PATH + '/user/register'
      return (
        <HttpGet
          url={url}
          headers={header}
          render={(result) => (
            <div>
              <HttpGetSwitch
                result={result}
                onLoading={() => <div><Spin id='spin' tip='Checking database...' /></div>}
                onJson={json => {
                  const authString = `${data.username}:${data.password}`
                  const encoded = window.btoa(authString)
                  cookies.set('auth', encoded)
                  return (
                    <div>
                      <Redirect to='/' />
                    </div>
                  )
                }
                }
                onError={_ => {
                  message.error('Error in register, try again!')
                  this.setState({redirect: false})
                  return null
                }}
              />
            </div>
          )} />

      )
    }
    const { getFieldDecorator } = this.props.form
    return (
      <div className='App'>
        <header className='App-header'>
          <img src={logo} className='App-logo' alt='logo' />
          <h1 className='App-title'>Welcome to YACMA <small>(Yet Another Checklist Management Application)</small></h1>
        </header>
        <p className='App-intro' >
          <div>
            <Form onSubmit={this.handleSubmit} className='login-form' >
              <Form.Item>
                {getFieldDecorator('username', {
                  rules: [{ required: true, message: 'Please input your username!' }]
                })(
                  <Input
                    name='username'
                    placeholder='Username'
                  />
                )}
              </Form.Item>
              <Form.Item >
                {getFieldDecorator('email', {
                  rules: [{ required: true, message: 'Please input your email!' }]
                })(
                  <Input
                    name='email'
                    placeholder='Email'
                  />
                )}
              </Form.Item>
              <Form.Item >
                {getFieldDecorator('password', {
                  rules: [{ required: true, message: 'Please input your password!' }]
                })(
                  <Input
                    name='password'
                    placeholder='Password'
                    type='password'
                  />
                )}
              </Form.Item>
              <Form.Item >
                {getFieldDecorator('familyName', {
                  rules: [{ required: true, message: 'Please input your family name!' }]
                })(
                  <Input
                    name='familyName'
                    placeholder='Family Name'
                  />
                )}
              </Form.Item>
              <Form.Item >
                {getFieldDecorator('givenName', {
                  rules: [{ required: true, message: 'Please input your given name!' }]
                })(
                  <Input
                    name='givenName'
                    placeholder='Given Name'
                    value={this.state.givenName} />
                )}
              </Form.Item>
              <Form.Item >
                <Button type='primary' htmlType='submit' className='login-form-button'>Register</Button>
              </Form.Item>
            </Form>
          </div>
        </p>
      </div>
    )
  }
}

const WrappedRegistrationForm = Form.create()(RegisterForm)
export default WrappedRegistrationForm
