import React from 'react'
import ReactDOM from 'react-dom'

function example () {
  ReactDOM.render(
    React.createElement('h1', {}, 'Hello world'),
    document.getElementById('app')
  )
}

example()
