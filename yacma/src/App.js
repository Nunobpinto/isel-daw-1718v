import React from 'react'
import { BrowserRouter, Route, Switch } from 'react-router-dom'
import Home from './components/Home'
import Login from './components/Login'
import Register from './components/Register'
import ProtectedRoute from './components/ProtectedRoute'
import Logout from './components/Logout'
import Checklists from './components/Checklists'
import Templates from './components/Templates'
import Checklist from './components/Checklist'
import Template from './components/Template'

export default () => (
  <BrowserRouter>
    <Switch>
      <Route exact path='/login' render={props => <Login {...props} />} />
      <Route path='/register' render={props => <Register {...props} />} />
      <ProtectedRoute exact path='/logout' component={Logout} />
      <ProtectedRoute exact path='/' component={Home} />
      <ProtectedRoute exact path='/checklists' component={Checklists} />
      <ProtectedRoute exact path='/checklists/:checklistId' component={Checklist} />
      <ProtectedRoute exact path='/checklists/:checklistId/items/:itemId' component={Home} />
      <ProtectedRoute exact path='/templates' component={Templates} />
      <ProtectedRoute exact path='/templates/:templateId' component={Template} />
      <ProtectedRoute exact path='/templates/:templateId/items/:itemId' component={Home} />
    </Switch>
  </BrowserRouter>
)
