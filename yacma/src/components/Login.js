import React from 'react'
import {Redirect, Link} from 'react-router-dom'
import {Form, Input, Button, Icon, Spin} from 'antd'
import logo from '../logo.svg'
import 'antd/dist/antd.css'
import config from '../config'
import Cookies from 'universal-cookie'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
const cookies = new Cookies()

class LoginForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      redirect: false
    }
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleSubmit (e) {
    e.preventDefault()
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const authString = `${values.username}:${values.password}`
        const encoded = window.btoa(authString)
        cookies.set('auth', encoded)
        this.setState(() => ({
          redirect: true
        }))
      }
    })
  }

  render () {
    const {redirect} = this.state
    if (redirect === true) {
      const cookie = cookies.get('auth')
      const header = {
        method: 'GET',
        headers: {
          'Authorization': `Basic ${cookie}`,
          'Access-Control-Allow-Origin': '*'
        }
      }
      const decoded = window.atob(cookie)
      const username = decoded.split(':')[0]
      const url = config.API_PATH + 'api/users/' + username
      return (
        <HttpGet
          url={url}
          headers={header}
          render={(result) => (
            <div>
              <HttpGetSwitch
                result={result}
                onLoading={() => <div><Spin id='spin' tip='Checking user credentials...' /></div>}
                onJson={json => (
                  <Redirect to='/' />
                )}
                onError={_ => (
                  <div>
                    <p> Error Logging In</p>
                    {this.setState({redirect: false})}
                    <Button>
                      <Link to='/login'>Try Again</Link>
                    </Button>
                  </div>
                )}
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
            <Form onSubmit={this.handleSubmit} className='login-form' id='formItem' >
              <Form.Item>
                {getFieldDecorator('username', {
                  rules: [{ required: true, message: 'Please input your username!' }]
                })(
                  <Input
                    prefix={<Icon type='user' style={{ color: 'rgba(0,0,0,.25)' }} />}
                    name='username'
                    placeholder='Username'
                    size='large'
                    value={this.state.username} />
                )}
              </Form.Item>
              <Form.Item>
                {getFieldDecorator('password', {
                  rules: [{ required: true, message: 'Please input your Password!' }]
                })(
                  <Input
                    prefix={<Icon type='lock' style={{ color: 'rgba(0,0,0,.25)' }} />}
                    type='password'
                    placeholder='Password'
                    size='large'
                    value={this.state.password} />
                )}
              </Form.Item>
              <Form.Item>
                <Button type='primary' htmlType='submit' className='login-form-button'>
              Log in
                </Button>
                <br />
                Or create an account
                <Link to='/register'> Register </Link>
              </Form.Item>
            </Form>
          </div>
        </p>
      </div>
    )
  }
}

const WrappedNormalLoginForm = Form.create()(LoginForm)
export default WrappedNormalLoginForm
