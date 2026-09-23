import React from 'react';
import { useAuth } from '../context/AuthContext';

export const DashboardPage: React.FC = () => {
  const { user } = useAuth();

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Witaj w systemie VetCare, {user?.username}!</h1>
      <p>Wybierz opcję z menu wyżej, aby przejść do zarządzania usługami lub swoim profilem.</p>
    </div>
  );
};