import React from 'react';
import styles from './Alert.module.css';

interface AlertProps {
  message: string;
  type?: 'error' | 'success';
}

export const Alert: React.FC<AlertProps> = ({ message, type = 'error' }) => {
  return (
    <div className={`${styles.alert} ${styles[type]}`} role="alert" aria-live="polite">
      <svg className={styles.icon} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="12" cy="12" r="10" />
        <line x1="12" y1="8" x2="12" y2="12" />
        <line x1="12" y1="16" x2="12.01" y2="16" />
      </svg>
      <span>{message}</span>
    </div>
  );
};