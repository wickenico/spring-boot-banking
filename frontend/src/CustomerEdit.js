import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavBar';

class CustomerEdit extends Component {

	emptyItem = {
		id: '',
		name: '',
		firstName: ''
	};

	constructor(props) {
		super(props);
		this.state = {
			item: this.emptyItem
		};
		this.handleChange = this.handleChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	async componentDidMount() {
		if (this.props.match.params.id !== 'new') {
			const customer = await (await fetch(`/api/v1/customers/${this.props.match.params.id}`)).json();
			this.setState({ item: customer });
		}
	}

	handleChange(event) {
		const target = event.target;
		const value = target.value;
		const name = target.name;
		let item = { ...this.state.item };
		item[name] = value;
		this.setState({ item });
	}

	async handleSubmit(event) {
		event.preventDefault();
		const { item } = this.state;

		await fetch('/v1/api/customers/' + (item.id ? '/' + item.id : ''), {
			method: (item.id) ? 'PUT' : 'POST',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(item),
		});
		this.props.history.push('/v1/api/customers/');
	}
	
	ender() {
    const {item} = this.state;
    const title = <h2>{item.id ? 'Edit Customer' : 'Add Customer'}</h2>;

    return <div>
        <AppNavbar/>
        <Container>
            {title}
            <Form onSubmit={this.handleSubmit}>
                <FormGroup>
                    <Label for="name">Name</Label>
                    <Input type="text" name="name" id="name" value={item.name || ''}
                           onChange={this.handleChange} autoComplete="name"/>
                </FormGroup>
                <FormGroup>
                    <Label for="email">FirstName</Label>
                    <Input type="text" name="email" id="firstName" value={item.firstName || ''}
                           onChange={this.handleChange} autoComplete="firstName"/>
                </FormGroup>	
                <FormGroup>
                    <Button color="primary" type="submit">Save</Button>{' '}
                    <Button color="secondary" tag={Link} to="/v1/api/customers/">Cancel</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>
}

}
export default withRouter(CustomerEdit);