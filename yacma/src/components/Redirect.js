import React from 'react'
import { UserManager } from 'oidc-client'

export default class extends React.Component {
  componentDidMount () {
    const mgr = new UserManager({})
    mgr.signinPopupCallback()
  }
  render () {
    return (
      <h5>Waiting for redirect</h5>
    )
  }
}
