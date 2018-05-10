import React from 'react'
import {withRouter} from 'react-router-dom'

export default withRouter(({history}) => (
  <nav>
    <button onClick={() => history.push('/')} >Home</button>
    <button onClick={() => {
      window.localStorage.removeItem('basic')
      history.push('/')
    }}>
            Logout
    </button>
  </nav>
))
