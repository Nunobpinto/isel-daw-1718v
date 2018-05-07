import React from 'react'
import fetch from 'isomorphic-fetch'

export default class extends React.Component {
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
      <div>
        <input
          name='username'
          placeholder='Username'
          onChange={e => this.onChange(e)}
          value={this.state.username} />
        <input
          name='email'
          placeholder='Email'
          onChange={e => this.onChange(e)}
          value={this.state.email} />
        <input
          name='password'
          placeholder='Password'
          type='password'
          onChange={e => this.onChange(e)}
          value={this.state.password} />
        <input
          name='familyName'
          placeholder='Family Name'
          onChange={e => this.onChange(e)}
          value={this.state.familyName} />
        <input
          name='givenName'
          placeholder='Given Name'
          onChange={e => this.onChange(e)}
          value={this.state.givenName} />
        <br />
        <button onClick={this.onSubmit}>Register</button>
      </div>
    )
  }
}
