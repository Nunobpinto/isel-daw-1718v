import React from 'react'
import { Redirect } from 'react-router-dom'
import Cookies from 'universal-cookie'
import oidc from '../oidcConfig'
const cookies = new Cookies()

export default () => {
  cookies.remove('auth')
  oidc().signoutPopup().then(_ => true)
  return (
    <Redirect to={{ pathname: '/login' }} />
  )
}
