import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

export interface ClinicServiceCategoryResponse {
  id: string;
  name: string;
  description?: string;
}

export interface ClinicServiceResponse {
  id: string;
  name: string;
  slug?: string;
  description?: string;
  price?: number;
  image?: string;
}

export const getAllCategories = async (): Promise<ClinicServiceCategoryResponse[]> => {
  const response = await axios.get<ClinicServiceCategoryResponse[]>(`${API_BASE_URL}/clinic-service-categories`);
  return response.data;
};

export const getClinicServicesByCategory = async (categoryId: string): Promise<ClinicServiceResponse[]> => {
  const response = await axios.get<ClinicServiceResponse[]>(`${API_BASE_URL}/clinic-services/category/${categoryId}`);
  return response.data;
};

export const getClinicServiceById = async (id: string): Promise<ClinicServiceResponse> => {
  const response = await axios.get<ClinicServiceResponse>(`${API_BASE_URL}/clinic-services/${id}`);
  return response.data;
};