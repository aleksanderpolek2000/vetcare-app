export interface CertificateResponse {
  id: string;
  imagePath: string;
  title?: string;
  displayOrder?: number;
}

export interface EmployeeProfileResponse {
  id: string;
  firstName: string;
  lastName: string;
  description?: string;
  profileImage?: string;
  certificates: CertificateResponse[];
}

export interface UpdateEmployeeProfileRequest {
  firstName: string;
  lastName: string;
  description?: string;
  profileImage?: File;
}

export interface AddCertificateRequest {
  image: File;
  title?: string;
  displayOrder?: number;
}