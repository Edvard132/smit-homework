import './App.css';
import Navibar from './components/Navibar';
import { Routes, Route } from 'react-router-dom';
import Footer from './components/Footer';
import Landing from './pages/Landing';
import Dashboard from './pages/Dashboard';

function App() {
  return (
    <div className='App'>
      <Navibar />
      <Routes>
        <Route exact path='/' element={<Landing />}></Route>
        <Route path="/book" element={<Dashboard />}></Route>
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
