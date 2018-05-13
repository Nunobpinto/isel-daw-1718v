import React from 'react'
import config from '../config'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'
import Cookies from 'universal-cookie'
import { Redirect } from 'react-router-dom'
import { message, Form, Input, Button, Spin, Select } from 'antd'
const cookies = new Cookies()
const FormItem = Form.Item

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.checkInputs = this.checkInputs.bind(this)
    this.onChange = this.onChange.bind(this)
    this.onStateChange = this.onStateChange.bind(this)
    this.state = {
      name: '',
      description: '',
      state: '',
      url: this.props.url,
      redirect: false
    }
  }

  onChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  onStateChange (value) {
    this.setState({
      'state': value
    })
  }

  checkInputs () {
    return this.state.name.length > 0 && this.state.description.length > 0 && this.state.state.length > 0
  }

  render () {
    let {redirect} = this.state
    if (redirect === true) {
      const values = this.state
      const data = {
        name: values.name,
        description: values.description,
        state: values.state
      }
      const path = config.API_PATH + this.state.url
      const encoded = cookies.get('auth')
      const header = {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
          'Authorization': `Basic ${encoded}`,
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
                onLoading={() => <div><Spin id='spin' tip='Creating Template...' /></div>}
                onJson={json => {
                  this.setState({redirect: false})
                  return (
                    <div>
                      <Redirect to={{ pathname: `/checklists/${json.properties.checklistId}/items/${json.properties.itemId}` }} />
                    </div>
                  )
                }
                }
                onError={_ => {
                  message.error('Error in creating the item, try again!')
                  this.setState({redirect: false})
                  return null
                }}
              />
            </div>
          )} />
      )
    }
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
        <h1>Create a new Item</h1>
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
            label='State'
            {...formItemLayout}
          >
            <Select
              showSearch
              style={{ width: 200 }}
              placeholder='Select a state'
              optionFilterProp='children'
              onChange={this.onStateChange}
            >
              <Select.Option value='Completed'>Completed</Select.Option>
              <Select.Option value='Uncompleted'>Uncompleted</Select.Option>
            </Select>
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
