import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Button } from '../components/ui/Button';
import styles from './HomePage.module.css';

export const HomePage: React.FC = () => {
  const { isAuthenticated, userEmail } = useAuth();

  return (
    <div className={styles.container}>
      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <span className={styles.badge}>Przyjazna Przychodnia Weterynaryjna</span>
          <h1 className={styles.heroTitle}>Profesjonalna troska o Twojego pupila</h1>
          <p className={styles.heroDescription}>
            Oferujemy pełen zakres opieki weterynaryjnej – od profilaktyki po zaawansowane zabiegi chirurgiczne. Na miejscu i bez zbędnego czekania.
          </p>

          <div className={styles.heroActions}>
            {isAuthenticated ? (
              <Link to="/panel">
                <Button variant="primary">Przejdź do Moich Wizyt</Button>
              </Link>
            ) : (
              <>
                <Link to="/login">
                  <Button variant="primary">Zaloguj się, aby umówić wizytę</Button>
                </Link>
                <Link to="/uslugi">
                  <Button variant="outline">Zobacz Cennik</Button>
                </Link>
              </>
            )}
          </div>
        </div>
      </section>

      <section className={styles.features}>
        <div className={styles.featureCard}>
          <h3>Profilaktyka i Szczepienia</h3>
          <p>Kompleksowe badania okresowe oraz programy ochronne dostosowane do wieku zwierzęcia.</p>
        </div>
        <div className={styles.featureCard}>
          <h3>Chirurgia i Diagnostyka</h3>
          <p>Nowoczesne laboratorium, USG, RTG oraz sterylny blok operacyjny.</p>
        </div>
        <div className={styles.featureCard}>
          <h3>Całodobowa Pomoc</h3>
          <p>Ostry dyżur dla nagłych przypadków wymagających natychmiastowej interwencji.</p>
        </div>
      </section>
    </div>
  );
};