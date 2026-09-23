import { api } from './axiosInstance';

export interface CertificateResponse {
  id: string; // UUID
  name: string;
  imagePath?: string;
  issuedDate?: string;
}

export interface EmployeeProfileResponse {
  id: string; // UUID
  firstName: string;
  lastName: string;
  description?: string;
  profileImage?: string;
  certificates?: CertificateResponse[];
}

export const getMyProfile = async (): Promise<EmployeeProfileResponse> => {
  const response = await api.get<EmployeeProfileResponse>('/employees/me');
  return response.data;
};

export const updateMyProfile = async (formData: FormData): Promise<EmployeeProfileResponse> => {
  const response = await api.put<EmployeeProfileResponse>('/employees/me', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
};

export const addCertificate = async (formData: FormData): Promise<EmployeeProfileResponse> => {
  const response = await api.post<EmployeeProfileResponse>('/employees/me/certificates', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response.data;
};

export const deleteCertificate = async (certificateId: string): Promise<void> => {
  await api.delete(`/employees/me/certificates/${certificateId}`);
};