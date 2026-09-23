import { api } from './axiosInstance';
import { EmployeeProfileResponse } from './employeeApi';

export const createEmployeeForUser = async (
  userId: string,
  formData: FormData
): Promise<EmployeeProfileResponse> => {
  const response = await api.post<EmployeeProfileResponse>(
    `/admin/employees/users/${userId}`,
    formData,
    { headers: { 'Content-Type': 'multipart/form-data' } }
  );
  return response.data;
};

export const updateEmployeeByAdmin = async (
  employeeId: string,
  formData: FormData
): Promise<EmployeeProfileResponse> => {
  const response = await api.put<EmployeeProfileResponse>(
    `/admin/employees/${employeeId}`,
    formData,
    { headers: { 'Content-Type': 'multipart/form-data' } }
  );
  return response.data;
};

export const deleteEmployeeByAdmin = async (employeeId: string): Promise<void> => {
  await api.delete(`/admin/employees/${employeeId}`);
};