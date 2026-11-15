import React, { Component, type ErrorInfo, type ReactNode } from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './styles.css';

class AppErrorBoundary extends Component<{ children: ReactNode }, { hasError: boolean }> {
  state = { hasError: false };
  static getDerivedStateFromError() { return { hasError: true }; }
  componentDidCatch(error: Error, info: ErrorInfo) {
    console.error('PRITHVI AI render error:', error, info.componentStack);
  }
  render() {
    if (this.state.hasError) {
      return <main className="setup-screen"><section className="setup-card" role="alert"><h1>PRITHVI AI could not open this screen</h1><p>A page error occurred. Refresh the browser. If it continues, open Developer Tools → Console and share the first red error.</p><button className="primary-btn" onClick={() => window.location.reload()}>Reload application</button></section></main>;
    }
    return this.props.children;
  }
}

const root = document.getElementById('root');
if (!root) throw new Error('Root element #root was not found in index.html');
ReactDOM.createRoot(root).render(<React.StrictMode><AppErrorBoundary><App /></AppErrorBoundary></React.StrictMode>);
