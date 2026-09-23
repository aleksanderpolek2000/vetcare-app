import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/users';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  email: string;
}

export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  const response = await axios.post<AuthResponse>(`${API_URL}/login`, credentials, {
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return response.data;
};

export const loginApi = login;