import React from 'react'
import { BrowserRouter, Route, Switch } from 'react-router-dom'
import Home from './components/Home'
import Login from './components/Login'
import Register from './components/Register'
import ProtectedRoute from './components/ProtectedRoute'
import Logout from './components/Logout'

export default () => (
  <BrowserRouter>
    <Switch>
      <Route exact path='/login' render={props => <Login {...props} />} />
      <Route path='/register' render={props => <Register {...props} />} />
      <Route path='/logout' render={props => <Logout {...props} />} />
      <ProtectedRoute exact path='/' component={Home} />
      <ProtectedRoute exact path='/checklists' component={Home} />
      <ProtectedRoute exact path='/checklists/:checklistId' component={Home} />
      <ProtectedRoute exact path='/checklists/:checklistId/items/:itemId' component={Home} />
      <ProtectedRoute exact path='/templates' component={Home} />
      <ProtectedRoute exact path='/templates/:templateId' component={Home} />
      <ProtectedRoute exact path='/templates/:templateId/items/:itemId' component={Home} />
    </Switch>
  </BrowserRouter>
)
