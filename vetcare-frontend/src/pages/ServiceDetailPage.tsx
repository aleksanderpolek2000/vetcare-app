import React, { useEffect, useState } from 'react';
import { useParams, useLocation, Link } from 'react-router-dom';
import { getAllCategories, getClinicServicesByCategory } from '../api/clinicApi';
import type { ClinicServiceResponse } from '../api/clinicApi';

export const ServiceDetailPage: React.FC = () => {
  // Dopasowane do App.tsx -> :serviceId
  const { serviceId } = useParams<{ serviceId: string }>();
  const location = useLocation();

  const [service, setService] = useState<ClinicServiceResponse | null>(
    (location.state as { service?: ClinicServiceResponse })?.service || null
  );
  const [loading, setLoading] = useState<boolean>(!service);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (service) {
      setLoading(false);
      return;
    }

    let isMounted = true;
    const fetchServiceById = async () => {
      if (!serviceId) {
        if (isMounted) {
          setError("Brak identyfikatora lub slugu usługi w adresie URL.");
          setLoading(false);
        }
        return;
      }

      try {
        setLoading(true);
        setError(null);

        const categories = await getAllCategories();
        let foundService: ClinicServiceResponse | null = null;

        for (const cat of categories) {
          try {
            const services = await getClinicServicesByCategory(cat.id);
            // Sprawdzamy czy serviceId z URL pasuje do id lub slugu usługi
            const matched = services.find(s => s.id === serviceId || s.slug === serviceId);
            if (matched) {
              foundService = matched;
              break;
            }
          } catch (catErr) {
            console.warn(`Nie udało się pobrać usług dla kategorii ${cat.id}:`, catErr);
          }
        }

        if (!isMounted) return;

        if (foundService) {
          setService(foundService);
        } else {
          setError(`Nie znaleziono usługi dla identyfikatora: "${serviceId}".`);
        }
      } catch (err: any) {
        console.error("Krytyczny błąd podczas wyszukiwania usługi:", err);
        if (isMounted) {
          setError(`Wystąpił błąd podczas pobierania szczegółów usługi.`);
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    fetchServiceById();
    return () => {
      isMounted = false;
    };
  }, [serviceId, service]);

  if (loading) {
    return (
      <div style={{ padding: '40px', textAlign: 'center', fontFamily: 'sans-serif', fontSize: '1.2rem' }}>
        Ładowanie szczegółów usługi...
      </div>
    );
  }

  if (error || !service) {
    return (
      <div style={{ padding: '40px', maxWidth: '600px', margin: '0 auto', fontFamily: 'sans-serif', textAlign: 'center' }}>
        <div style={{ padding: '15px', backgroundColor: '#ffe6e6', color: '#d9534f', borderRadius: '5px', marginBottom: '20px' }}>
          {error || `Nie znaleziono usługi.`}
        </div>
        <Link
          to="/uslugi"
          style={{
            display: 'inline-block',
            padding: '10px 20px',
            backgroundColor: '#007bff',
            color: '#fff',
            textDecoration: 'none',
            borderRadius: '5px',
            fontWeight: 'bold'
          }}
        >
          &larr; Wróć do listy usług
        </Link>
      </div>
    );
  }

  const imageUrl = service.image && service.image !== 'empty'
    ? (service.image.startsWith('http') ? service.image : `http://localhost:8080/uploads/services/${service.image.replace(/^\/+/, '').replace(/^services\//, '')}`)
    : '';

  return (
    <div style={{ padding: '40px', maxWidth: '900px', margin: '0 auto', fontFamily: 'sans-serif' }}>
      <Link
        to="/uslugi"
        style={{
          display: 'inline-block',
          marginBottom: '20px',
          textDecoration: 'none',
          color: '#007bff',
          fontWeight: '500'
        }}
      >
        &larr; Wróć do listy usług
      </Link>

      <div style={{ border: '1px solid #ddd', padding: '30px', borderRadius: '8px', backgroundColor: '#fff', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
        <div style={{ display: 'flex', flexDirection: 'row', gap: '30px', alignItems: 'flex-start', flexWrap: 'wrap' }}>

          <div style={{ flex: '1 1 400px' }}>
            <h1 style={{ marginTop: 0, color: '#333', fontSize: '2rem' }}>{service.name}</h1>
            <h3 style={{ color: '#555', marginBottom: '10px', fontSize: '1.1rem' }}>Opis usługi:</h3>
            <p style={{ lineHeight: '1.6', color: '#666', whiteSpace: 'pre-line' }}>{service.description || 'Brak opisu.'}</p>
            {service.price !== undefined && (
              <p style={{ fontSize: '1.3rem', fontWeight: 'bold', color: '#2c3e50', marginTop: '20px' }}>
                Cena: {service.price} zł
              </p>
            )}
          </div>

          <div style={{ flex: '0 0 300px', maxWidth: '100%', textAlign: 'center' }}>
            {imageUrl ? (
              <img
                src={imageUrl}
                alt={service.name}
                style={{ width: '100%', maxHeight: '350px', borderRadius: '8px', objectFit: 'cover', border: '1px solid #eee', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}
              />
            ) : (
              <div style={{ padding: '40px 20px', background: '#f8f9fa', color: '#6c757d', borderRadius: '8px', border: '2px dashed #dee2e6', fontStyle: 'italic' }}>
                [Brak zdjęcia dla tej usługi]
              </div>
            )}
          </div>

        </div>
      </div>
    </div>
  );
};

export default ServiceDetailPage;