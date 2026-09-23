import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Navbar } from './components/layout/Navbar';
import { HomePage } from './pages/HomePage';
import { TeamPage } from './pages/TeamPage';
import { ContactPage } from './pages/ContactPage';
import { LoginPage } from './pages/LoginPage';
import { ClinicServicesPage } from './pages/ClinicServicesPage';
import { ServiceDetailPage } from './pages/ServiceDetailPage';

const AboutPage = () => (
  <div style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto' }}>
    <h1>O nas</h1>
    <p>Witaj w przychodni weterynaryjnej VetCare.</p>
  </div>
);

export function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Navbar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/uslugi" element={<ClinicServicesPage />} />
          {/* Ważne: szczegóły muszą być wyżej niż /uslugi/:categorySlug, żeby router nie potraktował ID usługi jako sluga kategorii */}
          <Route path="/uslugi/szczegoly/:serviceId" element={<ServiceDetailPage />} />
          <Route path="/uslugi/:categorySlug" element={<ClinicServicesPage />} />
          <Route path="/o-nas" element={<AboutPage />} />
          <Route path="/zespol" element={<TeamPage />} />
          <Route path="/kontakt" element={<ContactPage />} />
          <Route path="/login" element={<LoginPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;