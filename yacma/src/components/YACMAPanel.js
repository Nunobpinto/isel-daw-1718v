import React from 'react'
import logo from '../logo.svg'
import '../App.css'
import 'antd/dist/antd.css'

export default () => (
  <div className='App'>
    <header className='App-header'>
      <img src={logo} className='App-logo' alt='logo' />
      <h1 className='App-title'>Welcome to YACMA <small>(Yet Another Checklist Management Application)</small></h1>
    </header>
    <p className='App-intro' />
  </div>
)
