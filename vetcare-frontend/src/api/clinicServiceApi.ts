import api from './axios';
import type {
  ClinicServiceResponse,
  CreateClinicServiceRequest,
  UpdateClinicServiceRequest,
} from '../types/clinicService';

export const getClinicServicesByCategoryApi = async (categoryId: string): Promise<ClinicServiceResponse[]> => {
  const response = await api.get<ClinicServiceResponse[]>(`/clinic-services/category/${categoryId}`);
  return response.data;
};

export const createClinicServiceApi = async (data: CreateClinicServiceRequest): Promise<ClinicServiceResponse> => {
  const formData = new FormData();
  formData.append('name', data.name);
  if (data.description) formData.append('description', data.description);
  if (data.image) formData.append('image', data.image);
  formData.append('categoryId', data.categoryId);

  const response = await api.post<ClinicServiceResponse>('/clinic-services', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
};

export const updateClinicServiceApi = async (
  id: string,
  data: UpdateClinicServiceRequest
): Promise<ClinicServiceResponse> => {
  const formData = new FormData();
  formData.append('name', data.name);
  if (data.description) formData.append('description', data.description);
  formData.append('displayOrder', data.displayOrder.toString());
  formData.append('active', data.active.toString());
  if (data.image) formData.append('image', data.image);
  formData.append('categoryId', data.categoryId);

  const response = await api.put<ClinicServiceResponse>(`/clinic-services/${id}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
};

export const deleteClinicServiceApi = async (id: string): Promise<void> => {
  await api.delete(`/clinic-services/${id}`);
};