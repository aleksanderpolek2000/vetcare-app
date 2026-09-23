import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { api } from '../api/axiosInstance';
import {
  ChevronRight,
  Stethoscope,
  Search,
  CheckCircle2,
  Home,
  AlertCircle,
  Calendar
} from 'lucide-react';
import './ClinicServicesPage.css'; // <-- DEDYKOWANY ARKUSZ STYLI CSS

export interface ClinicServiceCategoryResponse {
  id: string;
  name: string;
  description?: string;
  slug: string;
  displayOrder?: number;
  active: boolean;
}

export interface ClinicServiceResponse {
  id: string;
  name: string;
  description?: string;
  image?: string;
  slug: string;
  displayOrder?: number;
  active: boolean;
  categoryId: string;
}

export const ClinicServicesPage: React.FC = () => {
  const { categorySlug } = useParams<{ categorySlug?: string }>();
  const navigate = useNavigate();

  const [categories, setCategories] = useState<ClinicServiceCategoryResponse[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<ClinicServiceCategoryResponse | null>(null);
  const [services, setServices] = useState<ClinicServiceResponse[]>([]);
  const [searchQuery, setSearchQuery] = useState<string>('');

  const [loadingCategories, setLoadingCategories] = useState<boolean>(true);
  const [loadingServices, setLoadingServices] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  // 1. Pobieranie kategorii
  useEffect(() => {
    const fetchCategories = async () => {
      setLoadingCategories(true);
      setError(null);
      try {
        const response = await api.get<ClinicServiceCategoryResponse[]>('/clinic-service-categories');
        const activeCategories = response.data
          .filter((cat) => cat.active !== false)
          .sort((a, b) => (a.displayOrder ?? 0) - (b.displayOrder ?? 0));

        setCategories(activeCategories);

        // Jeśli w URL nie podano kategorii, automatycznie wybierz pierwszą
        if (!categorySlug && activeCategories.length > 0) {
          navigate(`/uslugi/${activeCategories[0].slug}`, { replace: true });
        }
      } catch (err) {
        console.error('Błąd pobierania kategorii:', err);
        setError('Nie udało się wczytać kategorii usług.');
      } finally {
        setLoadingCategories(false);
      }
    };

    fetchCategories();
  }, [categorySlug, navigate]);

  // 2. Pobieranie usług po zmianie kategorii
  useEffect(() => {
    if (!categorySlug || categories.length === 0) return;

    const matchedCategory = categories.find(
      (c) => c.slug === categorySlug || c.id === categorySlug
    );

    if (matchedCategory) {
      setSelectedCategory(matchedCategory);
      const fetchServices = async () => {
        setLoadingServices(true);
        try {
          const response = await api.get<ClinicServiceResponse[]>(
            `/clinic-services/category/${matchedCategory.id}`
          );
          const activeServices = response.data
            .filter((srv) => srv.active !== false)
            .sort((a, b) => (a.displayOrder ?? 0) - (b.displayOrder ?? 0));

          setServices(activeServices);
        } catch (err) {
          console.error('Błąd pobierania usług:', err);
          setError('Nie udało się wczytać usług dla wybranej kategorii.');
        } finally {
          setLoadingServices(false);
        }
      };
      fetchServices();
    } else {
      setSelectedCategory(null);
      setError('Podana kategoria nie istnieje.');
    }
  }, [categorySlug, categories]);

  const filteredServices = services.filter(
    (srv) =>
      srv.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      (srv.description && srv.description.toLowerCase().includes(searchQuery.toLowerCase()))
  );

  return (
    <div className="services-container">
      <div className="services-wrapper">

        {/* Nawigacja (Breadcrumbs) */}
        <nav className="breadcrumbs">
          <Link to="/">
            <Home style={{ width: 14, height: 14, verticalAlign: 'middle' }} /> Strona główna
          </Link>

          <span>/</span>
          <span>Klinika</span>
          <span>/</span>
          <span className="current">Usługi weterynaryjne</span>
        </nav>

        {/* Baner Główny */}
        <div className="hero-banner">
          <span className="hero-badge">VetCare Services</span>
          <h1 className="hero-title">Zakres Usług i Zabiegów</h1>
          <p className="hero-subtitle">
            Oferujemy pełny zakres opieki weterynaryjnej dla Twojego pupila. Wybierz kategorię z panelu po lewej stronie, aby zobaczyć szczegółową ofertę.
          </p>
        </div>

        {/* Błąd połączenia */}
        {error && (
          <div style={{ background: '#fef2f2', border: '1px solid #fecaca', color: '#b91c1c', padding: '1rem', borderRadius: '12px', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <AlertCircle style={{ width: 18, height: 18 }} />
            <span>{error}</span>
          </div>
        )}

        {/* Układ 2-kolumnowy */}
        <div className="layout-grid">

          {/* LEWY PANEL - KATEGORIE */}
          <aside className="sidebar-card">
            <div className="sidebar-heading">Kategorie Usług</div>
            {loadingCategories ? (
              <div className="spinner" />
            ) : (
              <div>
                {categories.map((cat) => {
                  const isActive = selectedCategory?.id === cat.id;
                  return (
                    <Link
                      key={cat.id}
                      to={`/uslugi/${cat.slug}`}
                      className={`category-link ${isActive ? 'active' : ''}`}
                    >
                      <span>{cat.name}</span>
                      <ChevronRight style={{ width: 16, height: 16 }} />
                    </Link>
                  );
                })}
              </div>
            )}
          </aside>

          {/* PRAWY PANEL - LISTA USŁUG */}
          <main>
            {selectedCategory && (
              <div className="main-header-card">
                <div className="category-info">
                  <h2>{selectedCategory.name}</h2>
                  {selectedCategory.description && <p>{selectedCategory.description}</p>}
                </div>

                <div className="search-box">
                  <Search className="search-icon-svg" />
                  <input
                    type="text"
                    placeholder="Szukaj usługi..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                  />
                </div>
              </div>
            )}

            {loadingServices ? (
              <div className="spinner" />
            ) : (
              <div className="services-list">
                {filteredServices.map((service) => (
                  <div key={service.id} className="service-card">
                    <div className="service-main">
                      {service.image ? (
                        <img src={service.image} alt={service.name} className="service-img" />
                      ) : (
                        <div className="service-icon">
                          <CheckCircle2 style={{ width: 24, height: 24 }} />
                        </div>
                      )}

                      <div className="service-details">
                        <h3>{service.name}</h3>
                        <p>{service.description || 'Szczegółowych informacji na temat tego zabiegu udzielamy podczas konsultacji.'}</p>
                      </div>
                    </div>

                    <Link to="/wizyta/rezerwacja" className="btn-book">
                      Umów wizytę
                    </Link>
                  </div>
                ))}

                {filteredServices.length === 0 && !loadingServices && (
                  <div className="empty-state">
                    <Stethoscope style={{ width: 40, height: 40, margin: '0 auto 0.5rem auto', opacity: 0.4 }} />
                    <p style={{ margin: 0, fontWeight: 600 }}>Brak dostępnych usług</p>
                    <p style={{ margin: '0.25rem 0 0 0', fontSize: '0.85rem' }}>Wybierz inną kategorię lub zmień wpisaną frazę.</p>
                  </div>
                )}
              </div>
            )}
          </main>
        </div>
      </div>
    </div>
  );
};