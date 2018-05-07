import React from 'react'
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Redirect
} from 'react-router-dom'
import Home from './components/Home'
import Login from './components/Login'
import Register from './components/Register'
import Header from './components/Header'

const checkAuth = () => {
  const auth = window.localStorage.getItem('basic')
  if (!auth) return false
  return true
}

const AuthRoute = ({ component: Component }) => (
  <Route render={props => (
    checkAuth() ? (
      <Component {...props} />
    ) : (
      <Redirect to={{ pathname: '/login' }} />
    )
  )} />
)

export default () => {
  return (
    <Router>
      <div>
        <Switch>
          <Route exact path='/login' render={props => <Login {...props} />} />
          <Route path='/register' render={props => <Register {...props} />} />
          <AuthRoute exact path='/' component={Home} />
        </Switch>
      </div>
    </Router>
  )
}
