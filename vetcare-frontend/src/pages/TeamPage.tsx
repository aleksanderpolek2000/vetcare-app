import React, { useEffect, useState } from 'react';
import { api } from '../api/axiosInstance';
import { User } from 'lucide-react';
import styles from './TeamPage.module.css';

interface Certificate {
  id: string;
  name: string;
  year?: number | string;
}

interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  role: string;
  specialization?: string;
  description?: string;
  email?: string;
  phone?: string;
  imageUrl?: string;
  profileImage?: string;
  certificates?: Certificate[];
}

export const TeamPage: React.FC = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        setLoading(true);
        const response = await api.get<Employee[]>('/employees');
        setEmployees(response.data);
      } catch (err) {
        console.error('Błąd pobierania pracowników:', err);
        setError('Nie udało się pobrać listy pracowników.');
      } finally {
        setLoading(false);
      }
    };

    fetchEmployees();
  }, []);

  if (loading) {
    return (
      <div className={styles.container} style={{ textAlign: 'center', padding: '80px 20px', color: '#64748b' }}>
        Ładowanie zespołu...
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.container} style={{ textAlign: 'center', padding: '40px', color: '#dc2626', backgroundColor: '#fef2f2', borderRadius: '12px' }}>
        {error}
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Nasz Zespół</h1>
      <p className={styles.subtitle}>
        Poznaj wykwalifikowanych specjalistów dbających o zdrowie i dobre samopoczucie Twojego pupila.
      </p>

      <div className={styles.list}>
        {employees.map((employee) => {
          const rawImage = employee.imageUrl || employee.profileImage;
          const imageUrl = rawImage && rawImage !== 'empty'
            ? (rawImage.startsWith('http') ? rawImage : `http://localhost:8080/uploads/employees/${rawImage.replace(/^\/+/, '').replace(/^employees\//, '')}`)
            : '';

          return (
            <div key={employee.id} className={styles.cardHorizontal}>
              {/* Większe zdjęcie po lewej */}
              {imageUrl ? (
                <img
                  src={imageUrl}
                  alt={`${employee.firstName} ${employee.lastName}`}
                  className={styles.avatarLarge}
                />
              ) : (
                <div className={styles.avatarPlaceholderLarge}>
                  <User size={64} />
                </div>
              )}

              {/* Informacje po prawej: Imię na górze, opis niżej */}
              <div className={styles.content}>
                <h2 className={styles.name}>
                  {employee.firstName} {employee.lastName}
                </h2>
                
                <div className={styles.role}>
                  {employee.role} {employee.specialization ? `• ${employee.specialization}` : ''}
                </div>

                <p className={styles.bio}>
                  {employee.description || 'Brak opisu dla tego pracownika.'}
                </p>

                {employee.certificates && employee.certificates.length > 0 && (
                  <div className={styles.diplomas}>
                    <div className={styles.diplomasTitle}>Certyfikaty i Dyplomy</div>
                    <ul className={styles.diplomaList}>
                      {employee.certificates.map((cert) => (
                        <li key={cert.id} className={styles.diplomaItem}>
                          <span>{cert.name}</span>
                          {cert.year && <span className={styles.diplomaYear}> ({cert.year})</span>}
                        </li>
                      ))}
                    </ul>
                  </div>
                )}
              </div>
            </div>
          );
        })}
      </div>

      {employees.length === 0 && (
        <div style={{ textAlign: 'center', padding: '60px', color: '#64748b', background: '#fff', borderRadius: '12px', border: '1px solid #e2e8f0' }}>
          Brak pracowników do wyświetlenia.
        </div>
      )}
    </div>
  );
};

export default TeamPage;