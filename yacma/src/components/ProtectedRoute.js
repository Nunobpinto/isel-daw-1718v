import React from 'react'
import {
  Route,
  Redirect
} from 'react-router-dom'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

const checkAuth = () => {
  const auth = cookies.get('auth')
  if (!auth) return false
  return true
}
export default ({ component: Component }) => (
  <Route render={props => (
    checkAuth() ? (
      <Component {...props} />
    ) : (
      <Redirect to={{ pathname: '/login' }} />
    )
  )} />
)
