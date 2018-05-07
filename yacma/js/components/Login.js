import React from 'react'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      username: '',
      password: ''
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
    const encoded = window.btoa(authString)
    window.localStorage.setItem('basic', encoded)
    window.history.back()
  }

  render () {
    return (
      <div>
        <input
          name='username'
          placeholder='Username'
          onChange={ev => this.onChange(ev)}
          value={this.state.username} />
        <input
          name='password'
          placeholder='Password'
          type='password'
          onChange={e => this.onChange(e)}
          value={this.state.password} />
        <br />
        <button onClick={this.onSubmit}>Login</button>
      </div>
    )
  }
}
