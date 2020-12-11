import logo from './logo.svg';
import './App.css';
import React, {Component} from 'react';
import CakeList from './CakeList';
import ReactDOM from 'react-dom';

class App extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return ReactDOM.render(
      <CakeList />,
       document.getElementById('root'));
    }
}

export default App;
