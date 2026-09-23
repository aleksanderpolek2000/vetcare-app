import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Button } from '../ui/Button';
import { getServiceCategories, type ServiceCategory } from '../../api/publicApi';
import styles from './Navbar.module.css';

export const Navbar: React.FC = () => {
  const { isAuthenticated, userEmail, logout } = useAuth();
  const [categories, setCategories] = useState<ServiceCategory[]>([]);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    getServiceCategories()
      .then((data) => {
        if (Array.isArray(data)) {
          const sorted = [...data].sort(
            (a, b) => (a.displayOrder ?? 0) - (b.displayOrder ?? 0)
          );
          setCategories(sorted);
        }
      })
      .catch((err) => console.error('Błąd pobierania kategorii:', err));
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className={styles.header}>
      <div className={styles.container}>
        {/* Logo -> Strona główna */}
        <Link to="/" className={styles.brand}>
          <div className={styles.logoWrapper}>
            <svg
              className={styles.logoIcon}
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
            >
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14h2v-3h3v-2h-3V8h-2v3H8v2h3v3z" />
            </svg>
          </div>
          <span className={styles.brandName}>VetCare</span>
        </Link>

        {/* Nawigacja */}
        <nav className={styles.nav}>
          <Link to="/" className={styles.navLink}>
            Strona Główna
          </Link>

          {/* Rozwijane Menu Usług z użyciem Sluga */}
          <div
            className={styles.dropdownWrapper}
            onMouseEnter={() => setIsDropdownOpen(true)}
            onMouseLeave={() => setIsDropdownOpen(false)}
          >
            {/* Zmiana spana na Link -> Kliknięcie przenosi na /uslugi, a najechanie rozwija menu */}
            <Link
              to="/uslugi"
              className={styles.navLink}
              onClick={() => setIsDropdownOpen(false)}
            >
              Usługi ▾
            </Link>

            {isDropdownOpen && (
              <div className={styles.dropdownMenu}>
                {categories.length > 0 ? (
                  categories.map((cat) => (
                    <Link
                      key={cat.id}
                      to={`/uslugi/${cat.slug || cat.id}`}
                      className={styles.dropdownItem}
                      onClick={() => setIsDropdownOpen(false)}
                    >
                      {cat.name}
                    </Link>
                  ))
                ) : (
                  <span className={styles.dropdownEmpty}>Brak kategorii</span>
                )}
              </div>
            )}
          </div>

          <Link to="/o-nas" className={styles.navLink}>
            O nas
          </Link>
          <Link to="/zespol" className={styles.navLink}>
            Zespół
          </Link>
          <Link to="/kontakt" className={styles.navLink}>
            Kontakt
          </Link>

          {isAuthenticated && (
            <Link to="/panel" className={styles.navLinkHighlight}>
              Moje Wizyty
            </Link>
          )}
        </nav>

        {/* Akcje i Umów Wizytę */}
        <div className={styles.authZone}>
          <a
            href="https://www.wettermin.pl/lecznice/krakow/4387"
            target="_blank"
            rel="noopener noreferrer"
            style={{ textDecoration: 'none' }}
          >
            <Button variant="secondary" className={styles.appointmentBtn}>
              Umów wizytę
            </Button>
          </a>

          {isAuthenticated ? (
            <div className={styles.userProfile}>
              <span className={styles.userEmail} title={userEmail || ''}>
                {userEmail}
              </span>
              <Button
                variant="outline"
                onClick={handleLogout}
                className={styles.logoutBtn}
              >
                Wyloguj
              </Button>
            </div>
          ) : (
            <Link to="/login">
              <Button variant="primary" className={styles.loginBtn}>
                Zaloguj się
              </Button>
            </Link>
          )}
        </div>
      </div>
    </header>
  );
};