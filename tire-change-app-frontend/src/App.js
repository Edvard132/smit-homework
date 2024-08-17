import './App.css';
import Navibar from './components/Navibar';
import { Routes, Route } from 'react-router-dom';
import Footer from './components/Footer';
import Landing from './pages/Landing';
import Dashboard from './pages/Dashboard';
import NotFound from './pages/NotFound';

function App() {
  return (
    <div className='App'>
      <Navibar />
      <Routes>
        <Route exact path='/' element={<Landing />}></Route>
        <Route path="/book" element={<Dashboard />}></Route>
        <Route path="*" element={<NotFound />} />
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
