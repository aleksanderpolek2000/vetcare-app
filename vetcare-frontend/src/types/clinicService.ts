export interface ClinicServiceResponse {
  id: string;
  name: string;
  description?: string;
  image?: string;
  slug: string;
  createdAt: string;
  updatedAt: string;
  displayOrder?: number;
  createdBy?: string;
  active: boolean;
  categoryId: string;
}

export interface CreateClinicServiceRequest {
  name: string;
  description?: string;
  image?: File;
  categoryId: string;
}

export interface UpdateClinicServiceRequest {
  name: string;
  description?: string;
  displayOrder: number;
  active: boolean;
  image?: File;
  categoryId: string;
}