import React, { Component } from 'react';
import logo from './logo.svg';
import '../../styles/App.css';
import data from '../../json/promotion.json';

class App extends Component {

  render() {
    return (
      <div className="json-file">
        <h3>Import Json File Format.</h3>
        <div className="content-json"> 
          <table border="0" width="100%">
            <thead>
              <tr>
                <th>No</th>
                <th>Mobile Number</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Promotion</th>
                <th>Service Charge</th>
              </tr>
            </thead>
            <tbody>
              {
                data.map(function(row, idx) {
                  return ( 
                    <tr>
                      <td>{idx + 1}</td>
                      <td>{row.mobileNumber}</td>
                      <td>{row.startDate}</td>
                      <td>{row.endDate}</td>
                      <td>{row.promotion}</td>
                      <td className="right">{row.serviceCharge}</td>
                    </tr>
                  );
                })
              }
            </tbody>
          </table>
        </div>
      </div>
    );
  }
}

export default App;
