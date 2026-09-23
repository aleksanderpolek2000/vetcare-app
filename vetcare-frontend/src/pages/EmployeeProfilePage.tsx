import React, { useEffect, useState } from 'react';
import {
  getMyProfile,
  updateMyProfile,
  addCertificate,
  deleteCertificate,
  EmployeeProfileResponse,
} from '../api/employeeApi';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { Alert } from '../components/ui/Alert';
import styles from './EmployeeProfilePage.module.css';

export const EmployeeProfilePage: React.FC = () => {
  const [profile, setProfile] = useState<EmployeeProfileResponse | null>(null);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [description, setDescription] = useState('');
  const [profileImage, setProfileImage] = useState<File | null>(null);

  // Stan dla certyfikatu
  const [certName, setCertName] = useState('');
  const [certFile, setCertFile] = useState<File | null>(null);

  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);
  const [loading, setLoading] = useState(false);

  const fetchProfile = async () => {
    try {
      const data = await getMyProfile();
      setProfile(data);
      setFirstName(data.firstName || '');
      setLastName(data.lastName || '');
      setDescription(data.description || '');
    } catch {
      setMessage({ type: 'error', text: 'Nie udało się pobrać profilu pracownika.' });
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  const handleUpdateProfile = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setMessage(null);

    const formData = new FormData();
    formData.append('firstName', firstName);
    formData.append('lastName', lastName);
    formData.append('description', description);
    if (profileImage) {
      formData.append('profileImage', profileImage);
    }

    try {
      const updated = await updateMyProfile(formData);
      setProfile(updated);
      setMessage({ type: 'success', text: 'Profil został zaktualizowany pomyślnie.' });
    } catch {
      setMessage({ type: 'error', text: 'Błąd podczas aktualizacji profilu.' });
    } finally {
      setLoading(false);
    }
  };

  const handleAddCertificate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!certFile || !certName.trim()) return;

    const formData = new FormData();
    formData.append('name', certName);
    formData.append('file', certFile);

    try {
      const updated = await addCertificate(formData);
      setProfile(updated);
      setCertName('');
      setCertFile(null);
      setMessage({ type: 'success', text: 'Certyfikat został dodany.' });
    } catch {
      setMessage({ type: 'error', text: 'Błąd podczas dodawania certyfikatu.' });
    }
  };

  const handleDeleteCert = async (certId: string) => {
    try {
      await deleteCertificate(certId);
      setProfile((prev) =>
        prev
          ? {
              ...prev,
              certificates: prev.certificates?.filter((c) => c.id !== certId),
            }
          : null
      );
      setMessage({ type: 'success', text: 'Certyfikat został usunięty.' });
    } catch {
      setMessage({ type: 'error', text: 'Błąd podczas usuwania certyfikatu.' });
    }
  };

  return (
    <div className={styles.container}>
      <h1>Mój Profil Pracownika</h1>

      {message && <Alert type={message.type}>{message.text}</Alert>}

      <form onSubmit={handleUpdateProfile} className={styles.form}>
        <Input
          label="Imię"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
          required
        />
        <Input
          label="Nazwisko"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
          required
        />

        <div className={styles.field}>
          <label htmlFor="description" className={styles.label}>Opis / Bio</label>
          <textarea
            id="description"
            className={styles.textarea}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            rows={4}
          />
        </div>

        <div className={styles.field}>
          <label htmlFor="profileImage" className={styles.label}>Zdjęcie profilowe</label>
          <input
            id="profileImage"
            type="file"
            accept="image/*"
            onChange={(e) => setProfileImage(e.target.files?.[0] || null)}
          />
        </div>

        <Button type="submit" disabled={loading}>
          {loading ? 'Zapisywanie...' : 'Zapisz zmiany'}
        </Button>
      </form>

      <hr className={styles.divider} />

      <h2>Moje Certyfikaty i Dyplomy</h2>

      <form onSubmit={handleAddCertificate} className={styles.certForm}>
        <Input
          label="Nazwa certyfikatu"
          value={certName}
          onChange={(e) => setCertName(e.target.value)}
          required
        />
        <div className={styles.field}>
          <label htmlFor="certFile" className={styles.label}>Plik certyfikatu</label>
          <input
            id="certFile"
            type="file"
            onChange={(e) => setCertFile(e.target.files?.[0] || null)}
            required
          />
        </div>
        <Button type="submit" variant="secondary">Dodaj Certyfikat</Button>
      </form>

      <ul className={styles.certList}>
        {profile?.certificates?.map((cert) => (
          <li key={cert.id} className={styles.certItem}>
            <span>{cert.name}</span>
            <Button variant="outline" onClick={() => handleDeleteCert(cert.id)}>
              Usuń
            </Button>
          </li>
        ))}
      </ul>
    </div>
  );
};