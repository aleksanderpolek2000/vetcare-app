import { api } from './axiosInstance';

export interface ServiceCategory {
  id: string;
  name: string;
  slug: string;
  description?: string;
  displayOrder?: number;
}

export interface ServiceItem {
  id: string;
  name: string;
  slug?: string;
  description?: string;
  price?: number;
  durationMinutes?: number;
  categoryId: string;
  displayOrder?: number;
}


export interface Diploma {
  id?: string;
  title: string;
  issuer?: string;
  issueDate?: string;
  year?: number;
}

export interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  roleDescription?: string;
  bio?: string;
  photoUrl?: string;
  diplomas?: Diploma[];
}

export const getServiceCategories = async (): Promise<ServiceCategory[]> => {
  const response = await api.get<ServiceCategory[]>('/clinic-service-categories');
  return response.data;
};

export const getServicesByCategory = async (categoryIdOrSlug?: string): Promise<ServiceItem[]> => {
  const response = await api.get<ServiceItem[]>('/clinic-services', {
    params: categoryIdOrSlug ? { category: categoryIdOrSlug } : {},
  });
  return response.data;
};

// 3. Brakująca funkcja pobierająca listę pracowników zespołu
export const getAllEmployees = async (): Promise<Employee[]> => {
  const response = await api.get<Employee[]>('/employees');
  return response.data;
};