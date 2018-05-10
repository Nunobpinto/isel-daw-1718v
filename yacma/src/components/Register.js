import React from 'react'
import fetch from 'isomorphic-fetch'
import {Form, Input, Button} from 'antd'
import logo from '../logo.svg'
import '../App.css'
import 'antd/lib/button/style/css'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

class RegisterForm extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      username: '',
      password: '',
      email: '',
      familyName: '',
      givenName: ''
    }
    this.onChange = this.onChange.bind(this)
    this.onSubmit = this.onSubmit.bind(this)
  }

  onChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  onSubmit () {
    const authString = `${this.state.username}:${this.state.password}`
    // fazer pedido a API para registar user
  }

  render () {
    return (
      <div className='App'>
        <header className='App-header'>
          <img src={logo} className='App-logo' alt='logo' />
          <h1 className='App-title'>Welcome to YACMA <small>(Yet Another Checklist Management Application)</small></h1>
        </header>
        <p className='App-intro' >
          <div>
            <Form type='vertical'>
              <Form.Item>
                <Input
                  name='username'
                  placeholder='Username'
                  onChange={e => this.onChange(e)}
                  value={this.state.username} />
              </Form.Item>
              <Form.Item >
                <Input
                  name='email'
                  placeholder='Email'
                  onChange={e => this.onChange(e)}
                  value={this.state.email} />
              </Form.Item>
              <Form.Item >
                <Input
                  name='password'
                  placeholder='Password'
                  type='password'
                  onChange={e => this.onChange(e)}
                  value={this.state.password} />
              </Form.Item>
              <Form.Item >
                <Input
                  name='familyName'
                  placeholder='Family Name'
                  onChange={e => this.onChange(e)}
                  value={this.state.familyName} />
              </Form.Item>
              <Form.Item >
                <Input
                  name='givenName'
                  placeholder='Given Name'
                  onChange={e => this.onChange(e)}
                  value={this.state.givenName} />
              </Form.Item>
              <Form.Item >
                <Button type='primary' onClick={this.onSubmit}>Register</Button>
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
