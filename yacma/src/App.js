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
import Item from './components/Item'
import config from './config'

export default () => (
  <BrowserRouter>
    <Switch>
      <Route exact path='/login' render={props => <Login {...props} />} />
      <Route path='/register' render={props => <Register {...props} />} />
      <ProtectedRoute exact path={config.APP.URI_LOGOUT} component={Logout} />
      <ProtectedRoute exact path={config.APP.URI_HOME} component={Home} />
      <ProtectedRoute exact path={config.APP.URI_CHECKLIST_ALL} component={Checklists} />
      <ProtectedRoute exact path={config.APP.URI_CHECKLIST_DETAIL} component={Checklist} />
      <ProtectedRoute exact path={config.APP.URI_CHECKLIST_ITEM_DETAIL} component={Item} />
      <ProtectedRoute exact path={config.APP.URI_TEMPLATE_ALL} component={Templates} />
      <ProtectedRoute exact path={config.APP.URI_TEMPLATE_DETAIL} component={Template} />
      <ProtectedRoute exact path={config.APP.URI_TEMPLATE_ITEM_DETAIL} component={Item} />
    </Switch>
  </BrowserRouter>
)
