import React from 'react';
import styles from './ContactPage.module.css';

export const ContactPage: React.FC = () => {
  return (
    <div className={styles.container}>
      <h1>Kontakt</h1>
      <div className={styles.content}>
        <div className={styles.details}>
          <h2>Przychodnia Weterynaryjna VetCare</h2>
          <p><strong>Adres:</strong> ul. Przykladowa 12, Kraków</p>
          <p><strong>Telefon:</strong> +48 123 456 789</p>
          <p><strong>E-mail:</strong> kontakt@vetcare.pl</p>
          <p><strong>Godziny otwarcia:</strong> Pn - Pt: 8:00 - 20:00, Sob: 9:00 - 14:00</p>
        </div>

        {/* Osadzona mapa Google dla Przychodni Mruczek */}
        <div className={styles.mapContainer}>
          <iframe
            title="Lokalizacja Przychodni"
            src="https://maps.google.com/maps?q=50.0946201,20.0094818&z=17&output=embed"
            width="100%"
            height="350"
            style={{ border: 0, borderRadius: '12px' }}
            allowFullScreen={false}
            loading="lazy"
          ></iframe>
        </div>
      </div>
    </div>
  );
};