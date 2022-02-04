/*import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

class App extends Component {
  state = {
	isLoading: true,
	customers: []
  };

  async componentDidMount() {
	const response = await fetch('/api/v1/customers');
	const body = await response.json();
	this.setState({ customers: body, isLoading: false });
  }

  render() {
	const {customers, isLoading} = this.state;

	if (isLoading) {
	  return <p>Loading...</p>;
	}

	return (
	  <div className="App">
		<header className="App-header">
		  <img src={logo} className="App-logo" alt="logo" />
		  <div className="App-intro">
			<h2>JUG List</h2>
			{customers.map(customers =>
			  <div key={customers.customerId}>
				{customers.name} {customers.firstName}
			  </div>
			)}
		  </div>
		</header>
	  </div>
	);
  }
}

export default App; */

import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import CustomerList from './CustomerList';
import CustomerEdit from "./CustomerEdit";

class App extends Component {
	render() {
		return (
			<Router>
				<Switch>
					<Route path='/' exact={true} component={Home} />
					<Route path='/customers' exact={true} component={CustomerList} />
					<Route path='/customers/:id' component={CustomerEdit} />
				</Switch>
			</Router>
		)
	}
}

export default App;