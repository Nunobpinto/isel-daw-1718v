import React from 'react'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import { Redirect } from 'react-router-dom'
import { message, Form, Input, Button, DatePicker, Spin } from 'antd'
import moment from 'moment'
const cookies = new Cookies()
const FormItem = Form.Item

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.checkInputs = this.checkInputs.bind(this)
    this.onChange = this.onChange.bind(this)
    this.onDateChange = this.onDateChange.bind(this)
    this.state = {
      name: '',
      description: '',
      date: '',
      url: this.props.url,
      redirect: false
    }
  }

  onChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  onDateChange (field, value) {
    this.setState({
      [field]: value
    })
  }

  checkInputs () {
    return this.state.name.length > 0 && this.state.description.length > 0 && this.state.date.length > 0
  }

  render () {
    let {redirect} = this.state
    if (redirect === true) {
      const state = this.state
      const data = {
        name: state.name,
        description: state.description,
        completion_date: state.date
      }
      const path = config.API.PATH + this.state.url
      const token = cookies.get('auth')
      const header = {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
          'Authorization': `Bearer ${token}`,
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json'
        }
      }
      return (
        <HttpGet
          url={path}
          headers={header}
          render={(result) => (
            <div>
              <HttpGetSwitch
                result={result}
                onLoading={() => <div><Spin id='spin' tip='Creating Checklist...' /></div>}
                onJson={json => {
                  this.setState({redirect: false})
                  return (
                    <div>
                      <Redirect to={{ pathname: `/checklists/${json.properties.checklistId}` }} />
                    </div>
                  )
                }
                }
                onError={_ => {
                  message.error('Error in creating the checklist, try again!')
                  this.setState({redirect: false})
                  return null
                }}
              />
            </div>
          )} />
      )
    }
    const dateFormat = 'YYYY-MM-DD'
    const { formLayout } = this.state
    const formItemLayout = {
      labelCol: { span: 2 },
      wrapperCol: { span: 14 }
    }
    const buttonItemLayout = {
      wrapperCol: { span: 14, offset: 4 }
    }
    return (
      <div>
        <h1>Create a new List</h1>
        <Form layout={formLayout}>
          <FormItem
            label='Name'
            {...formItemLayout}
          >
            <Input
              placeholder='input name'
              name='name'
              onChange={this.onChange}
              value={this.state.name}
            />
          </FormItem>
          <FormItem
            label='Description'
            {...formItemLayout}
          >
            <Input
              placeholder='input description'
              name='description'
              onChange={this.onChange}
              value={this.state.description}
            />
          </FormItem>
          <FormItem
            label='Completion Date'
            {...formItemLayout}
          >
            <DatePicker
              defaultValue={moment(new Date().toJSON().slice(0, 10), dateFormat)}
              format={dateFormat}
              name='date'
              onChange={(obj, value) => this.onDateChange('date', value)}
            />
          </FormItem>
          <FormItem {...buttonItemLayout}>
            <Button
              type='primary'
              disabled={!this.checkInputs()}
              onClick={() => {
                this.setState({redirect: true})
              }}
            >Submit</Button>
          </FormItem>
        </Form>
      </div>
    )
  }
}
