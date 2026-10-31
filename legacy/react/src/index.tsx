import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals.ts';

const rootElement = document.getElementById('root');

if (!rootElement) {
  throw new Error("No se encontró el elemento root");
}

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  rootElement
);

/// If you want to start measuring performance in your application, pass in a function
// to log the results (e.g.: reportWebVitals(console.log))
// or send to an analysis endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals((metric: any) => {
  console.log(metric);
});
