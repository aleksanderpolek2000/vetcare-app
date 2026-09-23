import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getAllCategories, getClinicServicesByCategory } from '../api/clinicApi';
import type { ClinicServiceCategoryResponse, ClinicServiceResponse } from '../api/clinicApi';

export const ClinicServicesPage: React.FC = () => {
  const { categorySlug } = useParams<{ categorySlug?: string }>();

  const [categories, setCategories] = useState<ClinicServiceCategoryResponse[]>([]);
  const [selectedCategoryId, setSelectedCategoryId] = useState<string | null>(null);
  const [services, setServices] = useState<ClinicServiceResponse[]>([]);
  const [loadingCategories, setLoadingCategories] = useState<boolean>(true);
  const [loadingServices, setLoadingServices] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  // 1. Pobieranie kategorii przy montowaniu komponentu
  useEffect(() => {
    let isMounted = true;
    const fetchCategories = async () => {
      try {
        setLoadingCategories(true);
        const data = await getAllCategories();
        if (!isMounted) return;

        setCategories(data);

        if (data && data.length > 0) {
          let targetCategory = data[0];
          if (categorySlug) {
            const found = data.find(c => c.name.toLowerCase().replace(/\s+/g, '-') === categorySlug || c.id === categorySlug);
            if (found) targetCategory = found;
          }
          setSelectedCategoryId(targetCategory.id);
        }
      } catch (err) {
        console.error("Błąd pobierania kategorii:", err);
        if (isMounted) setError("Nie udało się pobrać listy kategorii.");
      } finally {
        if (isMounted) setLoadingCategories(false);
      }
    };

    fetchCategories();
    return () => { isMounted = false; };
  }, [categorySlug]);

  // 2. Pobieranie listy usług TYLKO wtedy, gdy selectedCategoryId jest faktycznie ustawione
  useEffect(() => {
    if (!selectedCategoryId) {
      return;
    }

    let isMounted = true;
    const fetchServices = async () => {
      try {
        setLoadingServices(true);
        setError(null);

        const data = await getClinicServicesByCategory(selectedCategoryId);
        if (!isMounted) return;
        setServices(data || []);
      } catch (err) {
        console.error("Błąd pobierania listy usług:", err);
        if (isMounted) {
          setError("Nie udało się pobrać listy usług dla wybranej kategorii.");
          setServices([]);
        }
      } finally {
        if (isMounted) setLoadingServices(false);
      }
    };

    fetchServices();
    return () => { isMounted = false; };
  }, [selectedCategoryId]);

  if (loadingCategories) {
    return <div style={{ padding: '40px', textAlign: 'center', fontFamily: 'sans-serif' }}>Ładowanie kategorii usług...</div>;
  }

  return (
    <div style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto', fontFamily: 'sans-serif' }}>
      <h1 style={{ marginBottom: '20px', color: '#333' }}>Nasze Usługi Weterynaryjne</h1>

      {/* Pasek wyboru kategorii */}
      <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', marginBottom: '30px', borderBottom: '1px solid #ddd', paddingBottom: '15px' }}>
        {categories.map((cat) => {
          const isSelected = cat.id === selectedCategoryId;
          return (
            <button
              key={cat.id}
              onClick={() => setSelectedCategoryId(cat.id)}
              style={{
                padding: '10px 20px',
                backgroundColor: isSelected ? '#007bff' : '#f8f9fa',
                color: isSelected ? '#fff' : '#333',
                border: '1px solid',
                borderColor: isSelected ? '#007bff' : '#ccc',
                borderRadius: '5px',
                cursor: 'pointer',
                fontWeight: isSelected ? 'bold' : 'normal',
                transition: 'all 0.2s'
              }}
            >
              {cat.name}
            </button>
          );
        })}
      </div>

      {error && (
        <div style={{ padding: '15px', backgroundColor: '#ffe6e6', color: '#d9534f', borderRadius: '5px', marginBottom: '20px' }}>
          {error}
        </div>
      )}

      {loadingServices ? (
        <div style={{ textAlign: 'center', padding: '40px', color: '#666' }}>Ładowanie usług...</div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px' }}>
          {services.length === 0 ? (
            <p style={{ color: '#666', fontStyle: 'italic' }}>Brak usług w tej kategorii.</p>
          ) : (
            services.map((service) => {
              const imageUrl = service.image && service.image !== 'empty'
                ? (service.image.startsWith('http') ? service.image : `http://localhost:8080/uploads/services/${service.image.replace(/^\/+/, '').replace(/^services\//, '')}`)
                : '';

              return (
                <div
                  key={service.id}
                  style={{
                    border: '1px solid #ddd',
                    borderRadius: '8px',
                    overflow: 'hidden',
                    backgroundColor: '#fff',
                    boxShadow: '0 2px 4px rgba(0,0,0,0.05)',
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between'
                  }}
                >
                  <div>
                    {imageUrl && (
                      <img
                        src={imageUrl}
                        alt={service.name}
                        style={{ width: '100%', height: '180px', objectFit: 'cover' }}
                      />
                    )}
                    <div style={{ padding: '20px' }}>
                      <h3 style={{ marginTop: 0, marginBottom: '10px', color: '#333' }}>{service.name}</h3>
                      <p style={{ color: '#666', fontSize: '0.95rem', lineHeight: '1.5', display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                        {service.description || 'Brak opisu.'}
                      </p>
                    </div>
                  </div>

                  <div style={{ padding: '15px 20px', backgroundColor: '#f9f9f9', borderTop: '1px solid #eee', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span style={{ fontWeight: 'bold', color: '#2c3e50', fontSize: '1.1rem' }}>
                      {service.price !== undefined ? `${service.price} zł` : ''}
                    </span>
                    <Link
                      to={`/uslugi/szczegoly/${service.slug || service.id}`}
                      state={{ service }}
                      style={{
                        padding: '8px 15px',
                        backgroundColor: '#007bff',
                        color: '#fff',
                        textDecoration: 'none',
                        borderRadius: '4px',
                        fontSize: '0.9rem',
                        fontWeight: '500'
                      }}
                    >
                      Szczegóły &rarr;
                    </Link>
                  </div>
                </div>
              );
            })
          )}
        </div>
      )}
    </div>
  );
};

export default ClinicServicesPage;