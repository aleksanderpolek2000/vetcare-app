import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login as loginApiCall } from '../api/authApi';
import { useAuth } from '../context/AuthContext';
import { Input } from '../components/ui/Input';
import { Button } from '../components/ui/Button';
import { Alert } from '../components/ui/Alert';
import styles from './LoginPage.module.css';

export const LoginPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const data = await loginApiCall({
        email: email.trim(),
        password: password.trim(),
      });

      login(data.token, data.email);
      navigate('/');
    } catch (err: any) {
      console.error('Błąd logowania:', err);
      if (err.response?.status === 400 || err.response?.status === 401) {
        setError('Niepoprawne dane logowania. Sprawdź e-mail i hasło.');
      } else {
        setError('Nie można połączyć się z serwerem VetCare. Spróbuj ponownie później.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className={styles.container}>
      <div className={styles.card}>
        <header className={styles.header}>
          <div className={styles.logoWrapper}>
            <svg className={styles.logoIcon} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14h2v-3h3v-2h-3V8h-2v3H8v2h3v3z" />
            </svg>
          </div>
          <h1 className={styles.title}>VetCare</h1>
          <p className={styles.subtitle}>System Zarządzania Przychodnią Weterynaryjną</p>
        </header>

        {error && <Alert message={error} type="error" />}

        <form onSubmit={handleSubmit} className={styles.form} noValidate>
          <Input
            label="Adres E-mail"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="weterynarz@vetcare.pl"
            required
            autoComplete="email"
          />

          <Input
            label="Hasło Dostępowe"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            required
            autoComplete="current-password"
          />

          <Button type="submit" isLoading={loading}>
            Zaloguj się do systemu
          </Button>
        </form>

        <footer className={styles.footer}>
          <p className={styles.footerText}>VetCare System v1.0 • Prawa zastrzeżone</p>
        </footer>
      </div>
    </main>
  );
};